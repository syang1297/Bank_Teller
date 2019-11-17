import React, { Component } from 'react';
import { BrowserRouter, Route} from "react-router-dom";

import NavBar from './components/navbar';
import Teller from './components/teller';
import ATM from './components/atm';
import Home from './components/home';

import './css/App.css';
import 'bootstrap/dist/css/bootstrap.min.css';
function App() {
  return (
    <div>
      <BrowserRouter>
        <div>
          <NavBar/>
          <Route exact path="/teller" component={Teller} />
          <Route exact path="/atm" component={ATM} />
          <Route exact path="/" component={Home} />
        </div>
      </BrowserRouter>
    </div>
  );
}

export default App;
