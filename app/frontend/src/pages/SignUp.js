import React from "react";
import { Link } from 'react-router-dom';
import { Card, Input, Button } from '@material-ui/core';

function SignUp() {
  return (
    <Card>
        <Input type="email" placeholder="email" />
        <Input type="password" placeholder="password" />
        <Input type="password" placeholder="password again" />
        <Button>Sign Up</Button>
      <Link to="/signin">Already have an account?</Link>
    </Card>
  );
}

export default SignUp;