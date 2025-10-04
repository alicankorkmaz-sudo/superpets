import { initializeApp } from 'firebase/app';
import { getAuth } from 'firebase/auth';

const firebaseConfig = {
    apiKey: "AIzaSyDNovPI7SfmvRnyC6gLy262xwLWIEagHL0",
    authDomain: "superpets-ee0ab.firebaseapp.com",
    projectId: "superpets-ee0ab",
    storageBucket: "superpets-ee0ab.firebasestorage.app",
    messagingSenderId: "902402679969",
    appId: "1:902402679969:web:e2dde560b40010abf43ce0",
    measurementId: "G-4CKLYPSLD9"
  };

export const app = initializeApp(firebaseConfig);
export const auth = getAuth(app);


