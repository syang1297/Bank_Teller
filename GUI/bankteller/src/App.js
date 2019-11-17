import React, { Component } from 'react';
import { BrowserRouter, Route} from "react-router-dom";

import NavBar from './components/navbar';
import Teller from './components/teller';

import 'bootstrap/dist/css/bootstrap.min.css';
function App() {
  return (
    <div>
      <BrowserRouter>
        <div>
          <NavBar/>
          <Route exact path="/" component={Teller} />
          
        </div>
      </BrowserRouter>
    </div>
  );
}

export default App;
