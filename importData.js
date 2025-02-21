const admin = require("firebase-admin");
const fs = require("fs");

// Initialize Firebase Admin SDK
const serviceAccount = require("./serviceAccountKey.json");

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
});

const db = admin.firestore();

// Load JSON file
const rawData = fs.readFileSync("firestore-export.json");
const data = JSON.parse(rawData);

async function importData() {
  for (const category in data) {
    for (const productKey in data[category]) {
      const product = data[category][productKey];
      await db.collection(category).doc(productKey).set(product);
      console.log(`✅ Added: ${productKey} to ${category}`);
    }
  }
  console.log("🔥 Firestore data import completed!");
}

importData().catch(console.error);
