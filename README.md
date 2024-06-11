# RizzPick

![99수정 1](https://github.com/RizzPick/RizzPick-backend/assets/114673187/e29caf7a-d3df-4d45-9c4e-5295a53d3e4d)

## 📎 https://rizzpick.com

## Project Introduction

> Starting with the question, "What are we going to do?" we aim to guide users to form deep and meaningful relationships through attractive date plans.
---
## ✅ Key Features

### **1. Sharing and Choosing Date Plans**
> Users can share their individually planned date ideas and choose plans from others.

### **2. Personalized Recommendations**

> Recommendations are made based on region and gender.

---
## 🗓 Project Duration
October 4, 2023 - November 15, 2023 (6 weeks)

---
## ⚙️ Service Architecture
![ServiceArc](https://github.com/RizzPick/RizzPick-backend/assets/114673187/8279253d-1b33-454b-ab92-b62182f049b2)

---
## 💬 Technical Decisions

| Technology             | Description                                                                                                                                                                                                                                       |
|------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Nginx**        | Added for HTTPS connections and high-volume image processing, chosen over Apache for better performance and high traffic handling.                                             |
| **GitHub Actions** | Added for HTTPS connections and high-volume image processing, chosen over Apache for better performance and high traffic handling.                                                                              |
| **Redis(EC2)**   | Suitable for temporary data and caching, storing frequently accessed data to enhance access speed. EC2 instance allows for desired OS, memory, storage, and CPU configuration, with vertical and horizontal scaling and access restriction through AWS security groups. |
| **Swagger**      | Applied for API documentation and testing to facilitate quick and intuitive communication between front-end and back-end.                                                          |
| **Docker Hub**   | Decided to deploy using Docker Hub to facilitate multi-container processing with Nginx in EC2 server instances, beneficial for catching errors in the compile stage before program execution.                                       |
| **QueryDSL**     | Used to simplify complex query statements for Soft Delete and user recommendation logic, and applied to GET requests excluding POST, UPDATE, DELETE.                                                                      |
| **WebSocket**    | Applied to provide real-time connection between server and client for real-time chat functionality.                                                                                                                                                        |
| **SSE(Redis PUB/SUB)** | Applied for real-time notifications, managing events on the server with PUB/SUB and delivering effective real-time notifications to subscribed clients through SSE.                                       |


---
## 📑 ERD
![RizzPickERD](https://github.com/RizzPick/RizzPick-backend/assets/114673187/89789c7e-8db7-492f-ad79-8dbea7e79331)
---
## 🛠Troubleshooting

<details>
<summary>Using Redis for User Recommendation Logic</summary>
<div markdown="2">

> Problem

To create a user recommendation logic, we planned to manage it by creating an entity called Recommendations. However, adding Recommendations per user was expected to generate too many queries in MySQL and send too many requests.

> Solution Attempt


By utilizing Redis, we stored the Recommendations entity created at user login and deleted it after a certain period or when the user logs out. However, even with Redis, the user profile had to be re-queried from MySQL. Storing user profiles in Redis caused issues with profile retrieval, and without definitive user recommendation filters within the MVP, there was no service or efficiency reason to use Redis.


> Solution

Use MySQL queries until user-specific settings for the user recommendation API are established.

</div>
</details>

<details>
<summary>WebSocket Chat Rooms Getting Deleted</summary>
<div markdown="3">

> Problem

While implementing real-time chat functionality using WebSocket, chat rooms in Redis were unexpectedly getting deleted or specific users were getting removed from chat rooms.


> Solution Attempt


The backend alone was insufficient to pinpoint the problem, so we began collaborating with the front-end team, mutually reviewing code to find the cause. Based on error logs, we conducted searches and consulted ChatGPT for resolution.

> Solution


From the error logs, a CustomException was observed in the validateChatRoomId method of ChatRoomService, indicating an issue with ChatRoomId validation.

```java
com.willyoubackend.global.exception.CustomException: null
        at com.willyoubackend.domain.websocket.service.ChatRoomService.validateChatRoomId(ChatRoomService.java:132) ...
        ...
        at java.base/java.lang.Thread.run(Thread.java:833) ...
```


The log showed that a CustomException occurred in the validateChatRoomId method of ChatRoomService. This method checks the validity of ChatRoomId, indicating an issue with this validation.

The key to solving the problem was modifying the ChatRoomId validation code below:

```java
// chatRoomId validation method
private void validateChatRoomId(Long chatRoomId) {
        if (chatRoomId == null) {
        throw new CustomException(ErrorCode.INVALID_CHATROOM_ID);
        }
        }
   ```

Temporarily removing this validation resolved the issue. Additionally, switching from Redis to MySQL for chat room data storage, considering Redis's volatility.

</div>
</details>

<details>
<summary>DB Query Optimization with QueryDSL</summary>
<div markdown="4">

> Problem

1. Long and inflexible query statements when fetching user lists using JPA.
2. Complex and variable requests for specific query retrievals.

> Solution Attempt

1. To address the issue, we introduced QueryDSL to write more concise and flexible query statements. While the code readability and flexibility improved, the query length remained similar to using only JPA.

> Solution

1. Optimized queries using QueryDSL's leftJoin and fetchJoin, reducing the number of executed queries by retrieving necessary data in a single query.
</div>
</details>

---

## 🧑🏻‍💻 Team Members

| Name      | Role  | GitHub Address                         |
|---------|-----|-------------------------------|
| Jung Woo-yong (B)	 | Team Leader	  | https://github.com/jwywoo     |
| So Seok-jin (F) | Deputy Leader	 | https://github.com/seokjin909    |
| Kim Yeon-su (F)	 | Team Member	  | https://github.com/Xeonxoo99    |
| Jeon Jin-woong (B)	 | Team Member	  | https://github.com/JJW11111   |
| Kim Woo-eung (B) | Team Member	  | https://github.com/Gimwooeung |
| Lee Jae-ha (B) | Team Member	  | https://github.com/jaeha0183  |


---
## [Front-end Github Link](https://github.com/RizzPick/RizzPick-frontEnd)

---
## [Back-end Github Link](https://github.com/RizzPick/RizzPick-backend)

---

