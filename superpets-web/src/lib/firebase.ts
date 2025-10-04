import { initializeApp } from 'firebase/app';
import { getAuth } from 'firebase/auth';

const firebaseConfig = {
    apiKey: "AIzaSyCW2K-_Rl6e2Ab6OpnWSnUlrjsWE1EA6Mk",
    authDomain: "superpets-67402.firebaseapp.com",
    projectId: "superpets-67402",
    storageBucket: "superpets-67402.firebasestorage.app",
    messagingSenderId: "1006891739815",
    appId: "1:1006891739815:web:0d65750c3d409595704982",
    measurementId: "G-Q2YFH2JWPJ"
  };

export const app = initializeApp(firebaseConfig);
export const auth = getAuth(app);


