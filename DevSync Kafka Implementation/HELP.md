# ** 📄 DevSync - A Public Messaging Forum**

📌 DevSync is a real-time messaging forum where users can send messages, participate in discussions, and communicate asynchronously using Kafka.

🔹 Built using Spring Boot, Kafka, and JWT Authentication.

🔹 Supports asynchronous, non-blocking messaging.

🔹 Implements JWT-based authentication & authorization.

🔹 Messages persist in Kafka for a limited time before being deleted.


## **🚀 Features**

✅ User Registration & Login (JWT Authentication)

✅ Role-Based Access Control (ADMIN, DEV)

✅ Send & Retrieve Messages via Kafka

✅ Kafka Handles Temporary Message Storage


## **🛠️ Tech Stack**

✅Java (Spring Boot)	    -> Backend framework

✅Spring Security + JWT	    -> Authentication & Authorization

✅Kafka	                    -> Message Queue (Temporary Storage)

✅Lombok	                -> Reduces boilerplate code

✅Postman	                -> API testing


## **📂 Project Structure**
![image][https://github.com/prasanna-soft-dev/Spring-Boot-Projects/blob/668f7abda11c4de39bf46ee5e38a79499cdc2665/DevSync%20Kafka%20Implementation/DevSync%20testing/Project%20Structure.png]

## **⚙️ Installation & Setup**

**🔹 Step 1: Clone the Repository**


**🔹 Step 2: Configure Kafka**


**🔹 Step 3: Run Kafka**

zookeeper-server-start.sh config/zookeeper.properties

kafka-server-start.sh config/server.properties

**🔹 Step 4: Run the Application**


## **🚀 How It Works**

1️⃣ Users Register & Login → Get a JWT Token

2️⃣ Users Join the Forum (/api/forum) → Token is verified

3️⃣ Users Send Messages (/api/forum/send) → Kafka stores messages temporarily



**⚠ Note:**

Kafka retains messages only for a set time (log.retention.hours=24).
If a user connects after retention expires, messages are lost.


## **📌 How Kafka Stores Messages (Temporary Storage)**

✅Kafka stores messages for a configurable retention period.

✅Messages are removed after the retention period expires.

✅If a user is offline, they can receive messages later (as long as the retention period allows).

**Kafka Retention Configuration**

🔹Kafka stores messages for some time before deleting them.

🔹You can configure it in Kafka's **server.properties**.

🔹 If a user connects within the retention period, they receive messages.

🔹 If a user connects after retention expires, messages are deleted.


# **🛠️ Contributors**

Prasanna T– Backend Developer

Open to Contributions! 🤝


