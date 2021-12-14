<meta content="width=device-width, initial-scale=1.0, minimum-scale=1" name="viewport" />
<link href='https://fonts.googleapis.com/css?family=Exo+2:200,300,400,600,900' rel='stylesheet' type='text/css'/>
<link href='https://fonts.googleapis.com/css?family=Open+Sans:300,400,600' rel='stylesheet' type='text/css'/>

<@include_page path="/templates/generic/head_generic.jsp"/>
<@include_page path="/templates/generic/jsonrequest_support.jsp"/>
<@include_page path="/templates/generic/jsonform_support.jsp"/>
<@include_page path="/templates/generic/scriptaculous_support.jsp"/>
<@include_page path="/templates/generic/message_adapter_support.jsp"/>

<style type="text/css">

  body {
    background: #f5f5f5;
    color: #2c2c2c;
    font-family: "Open Sans", Arial, sans-serif;
    font-size: 1rem;
    line-height: 1.375rem;
    margin: 0;
    padding: 0;
  }
  
  body * {
    box-sizing: border-box;
  }
  
  form {
    align-items: center;
    display: flex;
    justify-content: center;
    width: 100%;
  }
  
  .muikku-login-card-wrapper {
    align-items: center;
    bottom: 0;
    display: flex;
    height: 100%;
    justify-content: center;
    left: 0;
    position: absolute;
    right: 0;
    top: 0;
    width: 100%;
  }
  
  .muikku-login-card {
    display: flex;
    flex-direction: column;
    height: auto;
    margin: 10px;
    max-width: 500px;
    width: calc(100% + 20px);
  }
  
  .muikku-logo-container {
    align-items: center;
    display: flex;
    flex-flow: row nowrap;
    padding: 1.1rem;
  }
  
  .muikku-logo {
    height: 50px;
    width: auto;
  }
  
  .muikku-logo-text {
    color: #009fe3;
    font-family: "Exo 2", Arial, sans-serif;
    font-size: 2rem;
    font-weight: 900;
    line-height: 2rem;
    padding: 0 1rem; 
  }
  
  .muikku-login-container {
    background: #fff;
    border: solid 1px #e2e2e2;
    border-radius: 5px;
    box-shadow: 0 0 20px rgba(0,0,0,0.05);
    flex-grow: 1;
    font-weight: 400;
    line-height: 1.375rem;
    padding: 1.1rem;
    position: relative;
    text-align: left;
    width: 100%;
  }
  
  .muikku-login-title {
    color: #009fe3;
    font-size: 1.2rem;
    font-weight: 400;
    line-height: 1;
    margin: 0;
    overflow: hidden;
    padding-bottom: 1.1rem;
    text-overflow: ellipsis;
  }
  
  .muikku-login-container-row {
    align-items: flex-start;
    display: flex;
    flex-flow: column nowrap;
    padding: 10px 0;
  }
  
  .muikku-login-label {
    display: block;
    font-weight: 400;
    padding: 0 0 4px;
    width: 100%;
  }
  
  input[type="text"].muikku-login-input,
  input[type="password"].muikku-login-input {
    border: 1px solid #dadada;
    border-radius: 3px;
    color: #2c2c2c;
    font-family: "Open Sans", Arial, sans-serif;
    font-size: 1.125rem;
    font-weight: 300;
    height: auto;
    outline: none;
    padding: 0.5rem;
    width: 100%;
  }
  input[type="text"].muikku-login-input:focus,
  input[type="password"].muikku-login-input:focus {
    border: 1px solid #c4c4c4;
    box-shadow: 0 0 0 5px #f5f5f5;
  }
  
  .muikku-login-footer {
    align-items: center;
    background: #fff;
    display: flex;
    justify-content: space-between;
    padding: 1.6rem 0 0;
  }
  
  .external-login-button,
  input[type="submit"].muikku-login-button {
    align-items: center;
    background-color: #009fe3;
    border: 0;
    border-radius: 20px;
    color: #ffffff;
    cursor: pointer;
    display: flex;
    font-family: "Open Sans", Arial, sans-serif;
    font-size: 1rem;
    font-weight: 400;
    justify-content: center;
    margin: 0;
    overflow: hidden;
    padding: 5px 15px;
    text-decoration: none;
    text-overflow: ellipsis;
    text-transform: uppercase;
    transition: background-color 0.3s;
    user-select: none;
    white-space: nowrap;
  }
  
  .Google-oauth-login-button {
    background: url("../gfx/icons/32x32/google-auth-icon.png") no-repeat center center;
    background-size: 2.5rem;
    border-radius: 100%;
    height: 2.5rem;
    width: 2.5rem;
    padding: 0;
  }
  
  @media screen and (min-width: 48em) {
  
    .muikku-login-card {
      width: 400px;
    }
  
    .muikku-login-title {
      font-size: 1.625rem;
    }
  
    .muikku-logo-container {
      padding: 1.6rem;
    }
    
    .muikku-login-container {
      padding: 1.6rem;
    }
  
    .muikku-logo {
      height: 60px;
    }
    
    .muikku-login-footer {
      padding: 1.6rem 0 0;
    }
  }
 

</style>