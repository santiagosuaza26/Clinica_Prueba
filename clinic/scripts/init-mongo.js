// MongoDB initialization script for Clinic IPS
// This script runs when the MongoDB container starts for the first time

// Switch to the medical history database
db = db.getSiblingDB('clinic_medical_history');

// Create collections with validation rules
db.createCollection('medical_history', {
  validator: {
    $jsonSchema: {
      bsonType: "object",
      required: ["patientId", "createdAt"],
      properties: {
        patientId: {
          bsonType: "string",
          description: "Patient ID is required"
        },
        createdAt: {
          bsonType: "date",
          description: "Creation date is required"
        },
        updatedAt: {
          bsonType: "date",
          description: "Last update date"
        }
      }
    }
  }
});

db.createCollection('medical_visits', {
  validator: {
    $jsonSchema: {
      bsonType: "object",
      required: ["patientId", "doctorId", "visitDate"],
      properties: {
        patientId: {
          bsonType: "string",
          description: "Patient ID is required"
        },
        doctorId: {
          bsonType: "string",
          description: "Doctor ID is required"
        },
        visitDate: {
          bsonType: "date",
          description: "Visit date is required"
        }
      }
    }
  }
});

// Create indexes for better performance
db.medical_history.createIndex({ "patientId": 1 });
db.medical_history.createIndex({ "createdAt": -1 });
db.medical_history.createIndex({ "patientId": 1, "createdAt": -1 });

db.medical_visits.createIndex({ "patientId": 1 });
db.medical_visits.createIndex({ "doctorId": 1 });
db.medical_visits.createIndex({ "visitDate": -1 });
db.medical_visits.createIndex({ "patientId": 1, "visitDate": -1 });

// Create a user for the application (if not using root)
db.getSiblingDB('admin').createUser({
  user: 'clinic_user',
  pwd: 'clinic_pass',
  roles: [
    {
      role: 'readWrite',
      db: 'clinic_medical_history'
    }
  ]
});

// Log initialization
print('MongoDB database initialized for Clinic IPS');
print('Database: clinic_medical_history');
print('Collections created: medical_history, medical_visits');
print('Indexes created for optimal performance');