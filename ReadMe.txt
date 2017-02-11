This is a Client-Server project within package ClientServer
All 4 clients are having their own folder to store data files and Client.java files
Server.java file is present at the root level along with Message.java, which defines communication packet between client and server.

At root of the project folder, run following commands to compile java files:

javac Message.java Server.java	
javac Message.java client1/Client.java
javac Message.java client2/Client.java
javac Message.java client3/Client.java
javac Message.java client4/Client.java

One level above the root of the project folder, run following commands to run Server and 4 Clients classes:

java ClientServer.Server
java ClientServer.client1.Client
java ClientServer.client2.Client
java ClientServer.client3.Client
java ClientServer.client4.Client


Clients once started and connected can send commands in following format:

<MessageCast> <MessageType> <message/file> <blocking/unicast Client>

Example:
unicast message "Hello World" client1
broadcast message "Hello World" 
blockcast message "Hello World" client1

unicast file "../clientdata#.txt" client1
broadcast file "../clientdata#.txt" 
blockcast file "../clientdata#.txt" client1
