import React from "react";
import {Navbar, Nav} from 'react-bootstrap';

import "../css/navbar.css"

export default class NavBar extends React.Component {
  render() {
    return (
      <div>
        <Navbar variant="light" expand="xl" fixed="top" className="navbar">
          <Navbar.Brand href="/">
            <Navbar.Text className="navbarTitle">Bank of A&S</Navbar.Text>
          </Navbar.Brand>
          <Navbar.Collapse id="basic-navbar-nav" className="justify-content-end">
            <Nav>
              <Nav.Link href="/atm" className="link">ATM</Nav.Link>
              <Nav.Link href="/teller" className="link">TELLER</Nav.Link>
            </Nav>
          </Navbar.Collapse>
        </Navbar>
      </div>
    );
  }
}
