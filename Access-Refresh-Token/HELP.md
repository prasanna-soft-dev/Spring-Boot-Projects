# **** Spring Boot JWT Authentication with Refresh Token****

## **🚀 Project Overview**

This project implements JWT-based authentication in a Spring Boot application, including access tokens, refresh tokens stored in HTTP-only cookies, and role-based authentication. 
It ensures secure authentication by leveraging Spring Security and JWT.

## **Refresh Token: Why It Is Needed**

**Introduction**

A refresh token is used to obtain a new access token when the old one expires, without requiring the user to log in again. 
It enhances security and improves the user experience.

## **Problems Without a Refresh Token**

Short-Lived Access Tokens

1. Access tokens (JWTs) are usually short-lived (e.g., 15-30 minutes) for security reasons.
2. Without a refresh token, users must log in again after every expiration.

### **Security Risks**

1. Storing long-lived access tokens in local storage is a security risk (XSS attacks).
2. If an access token is leaked, an attacker can misuse it until it expires.

### **How Refresh Tokens Solve This**

✅ **Longer Validity**

The refresh token is stored securely (e.g., in an HTTP-only cookie) and has a longer expiration (e.g., 7 days, 30 days).

✅ **Automatic Reauthentication**

When the access token expires, the client (frontend) sends the refresh token to the backend.

The backend validates the refresh token and issues a new access token.

✅ **Improved Security**

Refresh tokens are not sent with every request (unlike access tokens).

They can be revoked if a user logs out or if a security breach occurs.

## **How It Works (Flow)**
1. [ ] 
2. [ ] User logs in → Backend issues:
3. [ ] 
4. [ ] Access Token (short-lived)
5. [ ] 
6. [ ] Refresh Token (long-lived, stored in HTTP-only cookie)
7. [ ] 
8. [ ] User makes API requests using the access token.
9. [ ] 
10. [ ] Access token expires → The client sends the refresh token to /auth/refresh.
11. [ ] 
12. [ ] Backend verifies the refresh token:
13. [ ] 
14. [ ] If valid → Issue a new access token.
15. [ ] 
16. [ ] If invalid/expired → User must log in again.
17. [ ] 
18. [ ] User continues using the app without logging in again.

## **⚙️ Tech Stack**

✅Java 17

✅Spring Boot 3

✅Spring Security

✅JWT (JSON Web Token)

✅Jakarta Servlet API

✅Spring Data JPA (for database interactions)

✅MySQL (or any relational database)

## **🔐 Features**

✅ User Registration & Login

✅ JWT Access Token Generation

✅ Secure Refresh Token stored in HTTP-Only Cookie

✅ Spring Security Integration

✅ Token Validation and Expiry Handling


## **📂 Folder Structure**
![image alt](https://github.com/prasanna-soft-dev/Spring-Boot-Projects/blob/878a855e87f15f40e96ec992d7aa1914b158f378/Access-Refresh-Token/refresh%20token%20testing/Screenshot%20(40).png)

## **🔐 Testing**
**Registration**
![image alt](https://github.com/prasanna-soft-dev/Spring-Boot-Projects/blob/5b71f5e81aa98590b687f2d4cd851bbf410d7456/Access-Refresh-Token/refresh%20token%20testing/Registration.png)
**login**
![image alt](https://github.com/prasanna-soft-dev/Spring-Boot-Projects/blob/5b71f5e81aa98590b687f2d4cd851bbf410d7456/Access-Refresh-Token/refresh%20token%20testing/Login.png)
**Access Token authentication**
![image alt](https://github.com/prasanna-soft-dev/Spring-Boot-Projects/blob/5b71f5e81aa98590b687f2d4cd851bbf410d7456/Access-Refresh-Token/refresh%20token%20testing/Access%20token%20Authentication.png)
**Refresh token stored in Http only cookies**
![image alt](https://github.com/prasanna-soft-dev/Spring-Boot-Projects/blob/5b71f5e81aa98590b687f2d4cd851bbf410d7456/Access-Refresh-Token/refresh%20token%20testing/refresh%20token%20stored%20in%20Httponly%20cookies.png)
**Token info**
![image alt](https://github.com/prasanna-soft-dev/Spring-Boot-Projects/blob/5b71f5e81aa98590b687f2d4cd851bbf410d7456/Access-Refresh-Token/refresh%20token%20testing/Token%20info.png)
