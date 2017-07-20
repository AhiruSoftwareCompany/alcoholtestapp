# alcoholtestapp



[Emoji icons provided free by EmojiOne](http://emojione.com/)


## Data saved by app on your phone

### Users-Array

An array with every user currently existing including his drinks (updated on startup/refresh button press)

```
[
  {
    name:String,
    isMale:boolean,
    age:int,
    weight:int,
    height:int,
    created:long,
    drinks:[
      [
        takingTime[long],
        {
          name:String,
          amount:double,
          percentage:double,
          image:String
        }
      ]
    ]
  }
]
```

In addition to that: the uid (creation date) from the last selected user

### Drinks-Array

```
[
  {
    name:String,
    amount:double,
    percentage:double,
    image:String
  },
  {
    name:Eigenes\nGetränk,
    amount:0,
    percentage:0,
    image:custom
  }
]
```

## Send Feedback

### Feedback

Example of the string sent by the app:
```json
{
  "Device":{
    "OsVer":"3.0.31-novafusion(6452af36f2)",
    "OsApiLvl":22,
     "Device":"golden (samsung, GT-I8190)",
     "Model":"GT-I8190 (cm_golden)"
   },
  "AppInfo":"Friendly name: 1.1, version code: 1",
  "LogTrace":"",
  "Sender":"Klaus",
  "SenderMail":"foo@bar.cool",
  "Message":"Hi, I have a problem! Can you help me??"
}
```

The `LogTrace` is for future use: Logging every exception into a file.
