// HTTP server that takes JSON feedback for the Alkomat 3000 app and turns it into a neat
// e-mail forwarded to the maintainer's address.
package main

import (
	"encoding/json"
	"flag"
	"fmt"
	"io/ioutil"
	"log"
	"net/http"
	"net/smtp"
	"os"
	"strings"
	"time"

	"gopkg.in/gomail.v1"
)

var sendMail = sendStdMail

var useLoginAuth = flag.Bool("L", false, "Use LOGIN auth")

func main() {
	flag.Parse()

	if *useLoginAuth {
		sendMail = sendLoginMail
	}

	t := time.Now()

	f, err := os.OpenFile("feedback"+t.Format("20060102-1504")+".log", os.O_WRONLY|os.O_CREATE|os.O_APPEND, 0644)
	if err != nil {
		log.Fatal(err)
	}
	defer f.Close()
	log.SetOutput(f)

	if FromAddr == "foo@example.com" {
		log.Fatal("Please set the constants in config.go and recompile")
	}

	if Passwd == "" {
		log.Fatal("Please enter the password in config.go and recompile")
	}

	http.HandleFunc("/", rootHandler)

	log.Println("Starting server on port", Port)
	log.Fatal(http.ListenAndServeTLS(":"+Port, CertificatePath, PrivateKeyPath, nil))
}

type Device struct {
	OSVer    string
	OSAPILvl int
	Device   string
	Model    string
}

func (d Device) String() string {
	return fmt.Sprintf("\tOS version: %s (API %d)\n\tDevice: %s\n\tModel: %s\n",
		d.OSVer, d.OSAPILvl, d.Device, d.Model)
}

type Feedback struct {
	Device     Device
	AppInfo    string
	LogTrace   string
	Sender     string
	SenderMail string
	Message    string
}

func rootHandler(w http.ResponseWriter, r *http.Request) {
	r.ParseForm()

	body, _ := ioutil.ReadAll(r.Body)
	fbstr := string(body)

	agent := r.Header.Get("user-agent")

	log.Println("Received feedback from", agent, "(", r.RemoteAddr, ")")

	decoder := json.NewDecoder(strings.NewReader(fbstr))
	var feedback Feedback
	err := decoder.Decode(&feedback)

	if err != nil {
		log.Println("JSON decode error:", err)
	}

	// Only show sender if one is mentioned.
	senderstr := ""
	if feedback.Sender != "" {
		senderstr = fmt.Sprintf("Sender: %s <%s>", feedback.Sender, feedback.SenderMail)
	}

	// The message written by the app user is indented by a single tab for clearer separation.
	msg := fmt.Sprintf("Alkomat 3000 Feedback\n%s\nDevice:\n%v\nApp: %s\nMessage:\n\n\t",
		senderstr, feedback.Device, feedback.AppInfo)
	msg += strings.Replace(feedback.Message, "\n", "\n\t", -1) + "\n"
	msg += "\nEnd of message\n"
	msg += "Log trace:\n" + feedback.LogTrace + "\n"

	err = sendMail(Subject, msg)
	if err != nil {
		log.Println("couldn't send mail:", err)
		return
	}
}

// sendStdMail sends a mail with the given subject and content. The content must have Unix
// newlines (\n) and end in a newline. These newlines will be converted to SMTP's CRLF
// automatically.
func sendStdMail(subject, content string) error {
	auth := smtp.PlainAuth("", FromAddr, Passwd, SMTPSrv)
	to := []string{ToAddr}
	date := time.Now().Format(time.RFC822Z)

	header := fmt.Sprintf("Date: %s\r\nFrom: %s\r\nTo: %s\r\nSubject: %s\r\n", date, FromAddr, ToAddr, subject)
	body := strings.Replace(content, "\n", "\r\n", -1)
	msg := []byte(header + "\r\n" + body)

	return smtp.SendMail(SMTPSrv+":"+Port, auth, FromAddr, to, msg)
}

// sendLoginMail uses the LOGIN auth implemented using gomail instead.
func sendLoginMail(subject, content string) error {
	auth := gomail.LoginAuth(FromAddr, Passwd, SMTPSrv)
	mailer := gomail.NewCustomMailer(SMTPSrv, auth)

	body := strings.Replace(content, "\n", "\r\n", -1)

	msg := gomail.NewMessage()
	msg.SetHeaders(map[string][]string{
		"From":    {FromAddr},
		"To":      {ToAddr},
		"Subject": {subject},
	})
	msg.SetDateHeader("Date", time.Now())
	msg.SetBody("text/plain", body)

	return mailer.Send(msg)
}
