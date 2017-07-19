package main

import (
	"fmt"
	"log"
	"net/http"
	"net/smtp"
	"strings"
	"time"
)

const (
	// address of the feedback bot
	FromAddr = "foo@example.com"

	// SMTP server of the feedback bot
	SMTPSrv = "smtp.example.com"

	// your mail address
	ToAddr = "bar@derp.com"

	// default subject line; fixed at the moment
	Subject = "Feedback"

	// port used by this server
	Port = "8080"
)

func main() {
	if FromAddr == "foo@example.com" {
		log.Fatal("Please set the constants in main.go and recompile (go install)")
	}

	if Passwd == "" {
		log.Fatal("Please enter the password in passwd.go and recompile (go install)")
	}

	http.HandleFunc("/feedback", feedbackHandler)

	log.Println("Starting server on port ", Port)
	log.Fatal(http.ListenAndServe(":"+Port, nil))
}

func feedbackHandler(w http.ResponseWriter, r *http.Request) {
	r.ParseForm()

	err := sendMail(Subject, "Hello, world!\n") // newline is significant
	if err != nil {
		fmt.Fprintln(w, "Error sending mail")
		return
	}

	fmt.Fprintln(w, "Mail sent")
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
