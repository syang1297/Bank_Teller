import React, { Component } from 'react'
import {Form, Button, Col, Row} from 'react-bootstrap'

import '../css/pages.css';
export default class Teller extends Component {
  checkTransactionPressed(){
    console.log('Button clicked is: Check Transaction');
  }

  monthlyStatementPressed(){
    console.log('Button clicked is: monthlyStatementPressed');
  }

  listClosedAccountsPressed(){
    console.log('Button clicked is: listClosedAccounts');
  }

  genDTERPressed(){
    console.log('Button clicked is: genDTER');
  }

  customerReportPressed(){
    console.log('Button clicked is: customerReport');
  }

  addInterestPressed(){
    console.log('Button clicked is: addInterest');
  }

  createAccountPressed(){
    console.log('Button clicked is: createAccount');
  }

  deleteClosedPressed(){
    console.log('Button clicked is: deleteClosed');
  }

  deleteTransactionsPressed(){
    console.log('Button clicked is: deleteTransactions');
  }

  render() {
      return(
        <div className="homebox">
          <div>
            <Row>
              <Button variant="light" size="lg" type="button" onClick={this.checkTransactionPressed.bind(this)} name="checkTransaction" active>
                Enter Check Transaction
              </Button>
              <Button variant="light" size="lg" type="button" onClick={this.monthlyStatementPressed.bind(this)} name="monthlyStatement" active>
                Generate Monthly Statement
              </Button>
              <Button variant="light" size="lg" type="button" onClick={this.listClosedAccountsPressed.bind(this)} name="listClosedAccounts" active>
                List Closed Accounts
              </Button>
              <Button variant="light" size="lg" type="button" onClick={this.genDTERPressed.bind(this)} name="genDTER" active>
                Generate DTER
              </Button>
            </Row>
            <Row>
              <Button variant="light" size="lg" type="button" onClick={this.customerReportPressed.bind(this)} name="customerReport" active>
                Customer Report
              </Button>
              <Button variant="light" size="lg" type="button" onClick={this.addInterestPressed.bind(this)} name="addInterest" active>
                Add Interest
              </Button>
              <Button variant="light" size="lg" type="button" onClick={this.createAccountPressed.bind(this)} name="createAccount" active>
                Create Account
              </Button>
              <Button variant="light" size="lg" type="button" onClick={this.deleteClosedPressed.bind(this)} name="deleteClosed" active>
                Delete Closed Accounts and Customers
              </Button>
              <Button variant="light" size="lg" type="button" onClick={this.deleteTransactionsPressed.bind(this)} name="deleteTransactions" active>
                Delete Transactions
              </Button>
            </Row>
          </div>
        </div>
    )
  }
}
