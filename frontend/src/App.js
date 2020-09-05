import React, { Component } from 'react';
import './App.css';
import InstructorApp from './component/InstructorApp';
import axios from 'axios'

const API_URL = 'http://localhost:8080/api'
const EMPLOYEE_API_URL = `${API_URL}/db`


class App extends Component {
  constructor(props) {
    super(props)
    this.state = {
        employees: [],
        message: null
    }
    this.refreshEmployee = this.refreshEmployee.bind(this)
}

componentDidMount() {
    this.refreshEmployee();
}

refreshEmployee() {
  axios.get(`${EMPLOYEE_API_URL}`)
        .then(
            response => {
                console.log(response.data[2]);
                this.setState({ employees: response.data })
            }
        )
}
  render() {
    return (
      <div className="container">
                <h3>All Employee</h3>
                <div className="container">
                    <table className="table">
                        <thead>
                            <tr>
                                <th>Id</th>
                                <th>Forename</th>
                                <th>Surname</th>
                                <th>Description</th>
                            </tr>
                        </thead>
                        <tbody>
                            {
                                this.state.employees.map(
                                    employee =>
                                        <tr key={employee[0]}>
                                            <td>{employee.id}</td>
                                            <td>{employee.forename}</td>
                                            <td>{employee.surname}</td>
                                            <td>{employee.description}</td>
                                        </tr>
                                )
                            }
                        </tbody>
                    </table>
                </div>
            </div>
    );
  }
}

export default App;