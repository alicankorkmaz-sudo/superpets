import { initializeApp } from 'firebase/app';
import { getAuth } from 'firebase/auth';

const firebaseConfig = {
    apiKey: "AIzaSyDj73JUiY2y52HdlDYODrueqUYP4_rraGI",
    authDomain: "superpets-a42c5.firebaseapp.com",
    projectId: "superpets-a42c5",
    storageBucket: "superpets-a42c5.firebasestorage.app",
    messagingSenderId: "543519872909",
    appId: "1:543519872909:web:54e55bf1ba4300ee03cfc4",
    measurementId: "G-Y1Y925YP0D"
  };

export const app = initializeApp(firebaseConfig);
export const auth = getAuth(app);


