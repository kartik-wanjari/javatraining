//Create databse
use script

//Create collection
db.createCollection("scriptExe")

//insert data to collection
db.scriptExe.insertMany([
    {_id:1,name:"Kartik",age:22,place:"Nagpur",hobbies:["Video games","Cricket"],address:{Street:"WHC road",locality:"Ravi nagar"}},
    {_id:2,name:"Sarvesh",age:33,place:"Mumbai",hobbies:["Guiter","Chess","Reading"],address:{Street:"Marine drive",locality:"Thane"}}])

//updating one document
db.scriptExe.updateOne({name:"Sarvesh"},{$set:{age:34,"address.locality":"Kalyan nagar"}})

//get all documents from collection
db.scriptExe.find().pretty()

//get a document
db.scriptExe.find({name:"Kartik"}).pretty()

//delete one document
db.scriptExe.deleteOne({name:"Kartik"})

//get all document
db.scriptExe.find().pretty()