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

type Feedback struct {
	Device  Device
	Sender  string
	Message string
}

func rootHandler(w http.ResponseWriter, r *http.Request) {
	r.ParseForm()

	fbstr := r.Form.Get("feedback")
	if fbstr == "" {
		log.Println("missing or empty feedback property")
		return
	}

	log.Println("received feedback from", r.RemoteAddr)

	decoder := json.NewDecoder(strings.NewReader(fbstr))
	var feedback Feedback
	err := decoder.Decode(&feedback)
	if err != nil {
		log.Println("JSON decode error:", err)
	}

	// The message written by the app user is indented by a single tab for clearer separation.
	msg := fmt.Sprintf("Alkomat 3000 Feedback\n\nSender: %s\nDevice: %+v\nMessage:\n\n\t", feedback.Sender, feedback.Device)
	msg += strings.Replace(feedback.Message, "\n", "\n\t", -1) + "\n"
	msg += "\nEnd of feedback\n"

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
