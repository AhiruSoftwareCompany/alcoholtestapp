// HTTP server that takes JSON feedback for the Alkomat 300 app and turns it into a neat
// e-mail forwarded to the maintainer's address.
package main

import (
	"encoding/json"
	"fmt"
	"log"
	"net/http"
	"net/smtp"
	"strings"
	"time"
)

func main() {
	if FromAddr == "foo@example.com" {
		log.Fatal("Please set the constants in config.go and recompile")
	}

	if Passwd == "" {
		log.Fatal("Please enter the password in config.go and recompile")
	}

	http.HandleFunc("/", rootHandler)

	log.Println("Starting server on port", Port)
	log.Fatal(http.ListenAndServe(":"+Port, nil))
}

type Device struct {
	OSVer      string
	OSAPILevel string
	Device     string
	Model      string
}

func (d Device) String() string {
	return fmt.Sprintf("\tOS version: %s (API %s)\n\tDevice: %s\n\tModel: %s\n")
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

	fbstr := r.Form.Get("feedback")
	if fbstr == "" {
		log.Println("missing or empty feedback property")
		return
	}

	log.Println("Received feedback from", r.RemoteAddr)

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
	msg := fmt.Sprintf("Alkomat 3000 Feedback\n\n%s\nDevice: %v\nApp: %s\nMessage:\n\n\t",
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

// sendMail sends a mail with the given subject and content. The content must have Unix
// newlines (\n) and end in a newline. These newlines will be converted to SMTP's CRLF
// automatically.
func sendMail(subject, content string) error {
	auth := smtp.PlainAuth("", FromAddr, Passwd, SMTPSrv)
	to := []string{ToAddr}
	date := time.Now().Format(time.RFC822Z)

	header := fmt.Sprintf("Date: %s\r\nFrom: %s\r\nTo: %s\r\nSubject: %s\r\n", date, FromAddr, ToAddr, subject)
	body := strings.Replace(content, "\n", "\r\n", -1)
	msg := []byte(header + "\r\n" + body)

	return smtp.SendMail(SMTPSrv+":587", auth, FromAddr, to, msg)
}
