# Hospital-Management-System
MySql:
CREATE DATABASE hospital;
USE hospital;

CREATE DATABASE hospital;
USE hospital;

CREATE TABLE patients(
id INT AUTO_INCREMENT PRIMARY KEY,
name VARCHAR(100),
age INT,
gender VARCHAR(10)
);

CREATE TABLE doctors(
id INT AUTO_INCREMENT PRIMARY KEY,
name VARCHAR(100),
specialization VARCHAR(100)
);

CREATE TABLE appointments(
id INT AUTO_INCREMENT PRIMARY KEY,
patient_id INT,
doctor_id INT,
appointment_date DATE,
appointment_time TIME,
FOREIGN KEY (patient_id) REFERENCES patients(id),
FOREIGN KEY (doctor_id) REFERENCES doctors(id)
);
select *from patients;
select *from appointments;
SELECT * FROM doctors;
DESCRIBE appointments;
ALTER TABLE appointments
ADD appointment_time TIME;
