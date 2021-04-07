import firebase from "firebase/app";
import "firebase/analytics";

import "firebase/auth";
import "firebase/firestore";

export default class FirebaseSetting {

  private static fbs: FirebaseSetting
  public static Instance(): FirebaseSetting {
    if (FirebaseSetting.fbs == null) {
      FirebaseSetting.fbs = new FirebaseSetting()
    }
    return FirebaseSetting.fbs
  }

  public static InitializeInstance(): FirebaseSetting {
    return FirebaseSetting.Instance().Initialize()
  }

  private firebaseConfig = {
    apiKey: "AIzaSyDzPjdXWmdcwef6VW3SZmIwTWvSN31B3cM",
    authDomain: "yougame-srd.firebaseapp.com",
    databaseURL: "https://yougame-srd.firebaseio.com",
    projectId: "yougame-srd",
    storageBucket: "yougame-srd.appspot.com",
    messagingSenderId: "25800147012",
    appId: "1:25800147012:web:ffc95797d15e6a0494bc11",
    measurementId: "G-HJLLDK3KYG"
  }

  public isInitialized = false
  private app!: firebase.app.App
  private store!: firebase.firestore.Firestore
  private auth!: firebase.auth.Auth

  private constructor() {
    console.log(`Created my config ${this.firebaseConfig}`);
  }

  public Initialize(): FirebaseSetting {
    if (!this.isInitialized) {
      this.app = firebase.initializeApp(this.firebaseConfig);
      firebase.analytics();
      this.isInitialized = true;
      console.log('Firebase was initialized')
    }
    return this
  }

  public getStore(): firebase.firestore.Firestore {
    if (this.isInitialized && this.store == null) {
      this.store = firebase.firestore()
    }
    return this.store
  }

}
