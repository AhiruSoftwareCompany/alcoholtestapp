# alcoholtestapp

[Emoji icons provided free by EmojiOne](http://emojione.com/)

## Data saved by app on your phone

### Users-Array

An array with every user currently existing including its drinks (updated on startup/resume/refresh button press)

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

In addition to that: the "`uid`" (simply the creation date (with milliseconds)) from the last selected user

### Drinks-Array

`MixtureImage` represents a image (as string). See [source](https://github.com/dieechtenilente/alcoholtestapp/blob/master/app/src/main/java/de/klaushackner/breathalyzer/model/MixtureImage.java) for more information.

```
[
  {
    name:String,
    amount:double,
    percentage:double,
    image:MixtureImage
  },
  {
    name:Eigenes\nGetränk,
    amount:0,
    percentage:0,
    image:MixtureImage.custom
  }
]
```

For custom recipies saved by the user:

```
[
  {
    name:String,
    amount:double,
    percentage:double,
    image":MixtureImage
  }
]
```

## Send Feedback

### Feedback

Example of the string sent by the app:

```
{
  Device:{
    OsVer:"3.0.31-novafusion(6452af36f2)",
    OsApiLvl:22,
    Device:"golden (samsung, GT-I8190)",
    Model:"GT-I8190 (cm_golden)"
   },
  AppInfo:"Friendly name: 1.1, version code: 1",
  LogTrace:"",
  Sender:"Francis alias Ajax",
  SenderMail:"foo@bar.cool",
  Message:"Hi, I have a problem! Can you help me??"
}
```

The `LogTrace` is for future use: Logging every exception into a file.
