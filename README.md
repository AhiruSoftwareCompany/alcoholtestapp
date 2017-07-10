# alcoholtestapp



[Emoji icons provided free by EmojiOne](http://emojione.com/)


## Data saved by app on your phone

### Users-Array

An array with every user currently existing including his drinks (updated on startup/refresh button press)

```json
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

```json
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
