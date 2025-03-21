# File Task API

This project provides an API to generate a sequence of integers between `start` and `end`, save them as a comma-separated values (CSV) file, retrieve the stored data, and delete the file.

## Features
- Save a sequence of integers to a file
- Retrieve stored integer sequences
- Delete the stored file
- Handles various edge cases with appropriate error responses

## API Endpoints

### **POST /file** - Save to file
**Description:** Saves a sequence of integers between `start` and `end` to a CSV file.

#### **Request**
- **URL:** `http://localhost:9000/file`
- **Method:** `POST`
- **Content-Type:** `application/json`
- **Body:**
  ```json
  {
    "start": 2,
    "end": 6
  }
  ```

#### **Responses**
- **200 OK:** Successfully saved the sequence.
  ```json
  {
    "status": "success"
  }
  ```
- **400 BAD REQUEST:** `start` is greater than `end`
  ```json
  {
    "status": "failed",
    "message": "Start must be less than or equal to end"
  }
  ```
- **400 BAD REQUEST:** Invalid data type
  ```json
  {
    "status": "failed",
    "message": "Validation failed",
    "errors": [
        {
            "field": "/start",
            "message": "Expected a number"
        }
    ]
  }
  ```
- **400 BAD REQUEST:** Missing `end` field
  ```json
  {
    "status": "failed",
    "message": "Validation failed",
    "errors": [
        {
            "field": "/end",
            "message": "This field is required"
        }
    ]
  }
  ```
- **400 BAD REQUEST:** Negative integer values
  ```json
  {
    "status": "failed",
    "message": "Start and end must be non-negative integers"
  }
  ```
- **403 FORBIDDEN:** Permission denied while writing to the file.
  ```json
  {
    "status": "failed",
    "message": "Permission denied"
  }
  ```
- **409 CONFLICT:** File is locked or in use by another process.
  ```json
  {
    "status": "failed",
    "message": "File is locked"
  }
  ```
- **413 PAYLOAD TOO LARGE:** File path exceeds the system limit.
  ```json
  {
    "status": "failed",
    "message": "File path too long"
  }
  ```
- **500 INTERNAL SERVER ERROR:** Generic I/O error.
  ```json
  {
    "status": "failed",
    "message": "I/O error: [error details]"
  }
  ```

### **GET /file** - Fetch from file
**Description:** Retrieves the stored sequence of integers from the file.

#### **Request**
- **URL:** `http://localhost:9000/file`
- **Method:** `GET`

#### **Responses**
- **200 OK:** File found and contains data.
  ```json
  {
      "res": [2, 3, 4, 5, 6]
  }
  ```
- **404 NOT FOUND:** File does not exist.
  ```json
  {
      "status": "failed",
      "message": "File not found"
  }
  ```
- **422 UNPROCESSABLE ENTITY:** File is empty.
  ```json
  {
      "status": "failed",
      "message": "I/O error: File is empty"
  }
  ```
- **422 UNPROCESSABLE ENTITY:** Invalid content found in the file.
  ```json
  {
      "status": "failed",
      "message": "Invalid file content: Non-integer value found"
  }
  ```
- **403 FORBIDDEN:** Permission denied while reading the file.
  ```json
  {
      "status": "failed",
      "message": "Permission denied"
  }
  ```
- **409 CONFLICT:** File is locked or in use.
  ```json
  {
      "status": "failed",
      "message": "File is locked"
  }
  ```

### **DELETE /file** - Delete file
**Description:** Deletes the stored file.

#### **Request**
- **URL:** `http://localhost:9000/file`
- **Method:** `DELETE`

#### **Responses**
- **200 OK:** File successfully deleted.
  ```json
  {
      "success": true
  }
  ```
- **404 NOT FOUND:** File does not exist.
  ```json
  {
      "status": "failed",
      "message": "File not found"
  }
  ```
- **403 FORBIDDEN:** Permission denied while deleting the file.
  ```json
  {
      "status": "failed",
      "message": "Permission denied"
  }
  ```
- **409 CONFLICT:** File is locked or in use.
  ```json
  {
      "status": "failed",
      "message": "File is locked"
  }
  ```

## **Errors Handled**
- **Start is greater than end:** Returns `400 Bad Request`
- **Missing required fields:** Returns `400 Bad Request`
- **Negative integer inputs:** Returns `400 Bad Request`
- **Invalid data types (e.g., strings instead of numbers):** Returns `400 Bad Request`
- **File not found when retrieving or deleting:** Returns `404 Not Found`
- **Empty file:** Returns `422 Unprocessable Entity`
- **Invalid content in the file:** Returns `422 Unprocessable Entity`
- **Permission issues when writing, reading, or deleting:** Returns `403 Forbidden`
- **File is locked or in use:** Returns `409 Conflict`
- **File path exceeds the system limit:** Returns `413 Payload Too Large`
- **Generic I/O error:** Returns `500 Internal Server Error`

## **Server Information**
- Base URL: `http://localhost:9000`
- Available Routes:
  - `POST /file` - Save to file
  - `GET /file` - Fetch from file
  - `DELETE /file` - Delete file

## **Usage**
- Ensure the server is running on `localhost:9000`.
- Use API clients like Postman or `curl` to send requests.
- Alternatively, test the APIs using the provided `apiDemo.scala.html` template, which serves as a frontend interface accessible at `http://localhost:9000/api-demo`.
