import React, { Component } from 'react'
import {Form, Button, Col, Row, ToggleButton,ToggleButtonGroup} from 'react-bootstrap'
import Cookies from 'universal-cookie';

import '../css/atm.css';

let cookies = new Cookies();

export default class ATM extends Component {
  constructor() {
    super()
    this.taxID = React.createRef();
    this.pin = React.createRef();
    this.errorMessage="dada"
    this.value=-1;
  }

  state={
    accountSelected:-1,
    showError:false
  }

  loginPressed(){
    if(this.taxID.current.value!=="test" || this.pin.current.value!=="test"){
      this.errorMessage="Wrong taxID or Pin"
      this.setState({
        showError:true
      })
    } else {
      cookies.set('taxID', '12345', { path: '/' });
      console.log(cookies.get("taxID"))
    }
  }
  handleAccountSelection(val){
    this.setState({
      accountSelected:val
    }, () => {
      console.log("state: " + this.state.accountSelected)
    })
   
  }

  
  depositPressed(){
    console.log('Button clicked is: Deposit');
  }

  topUpPressed(){
    console.log('Button clicked is: Top-Up');
  }

  withdrawalPressed(){
    console.log('Button clicked is: Withdrawal');
  }

  purchasePressed(){
    console.log('Button clicked is: Purchase');
  }

  transferPressed(){
    console.log('Button clicked is: Transfer');
  }

  collectPressed(){
    console.log('Button clicked is: Collect');
  }

  wirePressed(){
    console.log('Button clicked is: Wire');
  }

  payFriendPressed(){
    console.log('Button clicked is: Pay Friend');
  }

  render() {
    if(cookies.get("taxID")==null){
      return (
        <div>
          <div className="homeBox">
            <div className="homeHead">Log into ATM</div>
            <Row>
            <Col>
              <Form>
                <Form.Group controlId="formBasicTaxID" as={Col}>
                  <Form.Label className="formText">TaxID</Form.Label>
                  <div className="formField">
                    <Form.Control type="taxID" placeholder="Enter taxID" ref={this.taxID}/>
                  </div>
                </Form.Group>
                <Form.Group controlId="formBasicPin" as={Col}>
                  <Form.Label className="formText">Pin</Form.Label>
                  <div className="formField">
                    <Form.Control type="password" placeholder="Pin" ref={this.pin}/>
                  </div>
                </Form.Group>
                <Form.Group controlId="formBasicUPLogin" as={Col}>
                  <div className="formButton">
                    <Button variant="primary" type="button" onClick={this.loginPressed.bind(this)}>
                      Submit
                    </Button>
                  </div>
                </Form.Group>
              </Form>
            </Col>
            </Row>
  
            <div className={this.state.showError ? 'errorMessage' : 'hideMessage'}>{this.errorMessage}</div>
          </div>
        </div>
      )
    } else {
      
      return(
        <div className="homebox">
          <div className="accountsArea">
            <ToggleButtonGroup name="accounts" type="radio" value={this.value} onChange={this.handleAccountSelection.bind(this)}>
              <ToggleButton value={1}>Account 1</ToggleButton>
              <ToggleButton value={2}>Account 2</ToggleButton>
              <ToggleButton value={3}>Account 3</ToggleButton>
            </ToggleButtonGroup>
          </div>
          <div>
            <Row>
              <Button variant="light" size="lg" type="button" onClick={this.depositPressed.bind(this)} name="deposit" active>
                Deposit
              </Button>
              <Button variant="light" size="lg" type="button" onClick={this.topUpPressed.bind(this)} name="topUp" active>
                Top-Up
              </Button>
              <Button variant="light" size="lg" type="button" onClick={this.withdrawalPressed.bind(this)} name="withdrawal" active>
                Withdrawal
              </Button>
              <Button variant="light" size="lg" type="button" onClick={this.purchasePressed.bind(this)} name="purchase" active>
                Purchase
              </Button>
            </Row>
            <Row>
              <Button variant="light" size="lg" type="button" onClick={this.transferPressed.bind(this)} name="transfer" active>
                Transfer
              </Button>
              <Button variant="light" size="lg" type="button" onClick={this.collectPressed.bind(this)} name="collect" active>
                Collect
              </Button>
              <Button variant="light" size="lg" type="button" onClick={this.wirePressed.bind(this)} name="wire" active>
                Wire
              </Button>
              <Button variant="light" size="lg" type="button" onClick={this.payFriendPressed.bind(this)} name="payFriend" active>
                Pay Friend
              </Button>
            </Row>
          </div>
        </div>
      )
    }    
  }
}

