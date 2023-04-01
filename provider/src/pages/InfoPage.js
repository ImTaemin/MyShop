import {Alert, Button, Card, Col, Form, Row} from "react-bootstrap";
import {TableNav, TableTitle} from "../components/common/Table";
import React, {useCallback, useEffect, useState} from "react";
import "../assets/scss/info.scss";
import "../assets/scss/debounce.scss";
import {getInfo, updateInfo} from "../lib/api/info";
import {useNavigate} from "react-router-dom";
import {BiHomeAlt} from "react-icons/bi";
import client from "../lib/api/client";
import {Helmet} from "react-helmet-async";


const InfoPage = () => {
  const navigate = useNavigate();

  const [formData, setFormData] = useState({
    userId: '',
    password: '',
    modifyPassword: '',
    phone: '',
    brandName: '',
    createDate: '',
  });
  const {userId, password, modifyPassword, phone, brandName, createDate} = formData;

  const [error, setError] = useState('');

  useEffect(() =>{
    client.defaults.headers.common['X-AUTH-TOKEN'] = localStorage.getItem("accessToken");

    async function fetchInfo() {
      try{
        const response = await getInfo();

        if(response.status === 200) {
          setFormData({
            ...formData,
            userId: response.data.userId,
            phone: response.data.phone,
            brandName: response.data.brandName,
            createDate: response.data.createDate,
          })
        }
      } catch (e) {
        alert("회원정보가 없습니다.");
        navigate('/orders');
      }
    }

    fetchInfo();
  }, []);

  const inputHandler = useCallback((e) => {
    const {name, value} = e.target;

    setFormData({
      ...formData,
      [name]: value
    });

  }, [formData]);

  const submitHandler = async (e) => {
    e.preventDefault();

    setFormData({
      ...formData,
    });

    const response = await updateInfo(formData);
    console.log(response);

    setFormData({
      ...formData,
      password: '',
      modifyPassword: '',
    });
  }

  useEffect(() =>{
    if(error !== '') {
      setTimeout(() => {
        setError('');
      }, 2000);
    }
  }, [error]);


  return (
    <>
      {error && (
        <div className="alert-box">
          <Alert variant="danger" onClose={() => setError('')} dismissible>
            <Alert.Heading>{error}</Alert.Heading>
          </Alert>
        </div>
      )}

      <Helmet>
        <title>상점 관리</title>
      </Helmet>
      <Card>
        <Card.Header>
          <TableNav>
            <TableTitle><BiHomeAlt style={{marginRight: "0.6em"}} /><b>{brandName}</b>  {createDate}</TableTitle>
          </TableNav>
        </Card.Header>

        <Card.Body>
          <div className="info-form-container">
            <Form>
              <Form.Group as={Row} className="mb-3" controlId="formId">
                <Form.Label column sm={2}>
                  아이디
                </Form.Label>
                <Col sm={10}>
                  <Form.Control
                    name="userId"
                    type="text"
                    value={userId}
                    style={{background: "#eeeeee"}}
                    readOnly/>
                </Col>
              </Form.Group>

              <Form.Group as={Row} className="mb-3" controlId="formPassword">
                <Form.Label column sm={2}>
                  비밀번호
                </Form.Label>
                <Col sm={10}>
                  <Form.Control
                    name="password"
                    type="password"
                    value={password}
                    placeholder="비밀번호"
                    onChange={inputHandler}
                    required />
                </Col>
              </Form.Group>

              <Form.Group as={Row} className="mb-3" controlId="formCheckPassword">
                <Form.Label column sm={2}>
                  변경 비밀번호
                </Form.Label>
                <Col sm={10}>
                  <Form.Control
                    name="modifyPassword"
                    type="password"
                    value={modifyPassword}
                    placeholder="변경 비밀번호"
                    onChange={inputHandler}
                    required/>
                </Col>
              </Form.Group>

              <Form.Group as={Row} className="mb-3" controlId="formPhone">
                <Form.Label column sm={2}>
                  휴대폰 정보 변경
                </Form.Label>
                <Col sm={10}>
                  <Form.Control
                    name="phone"
                    type="text"
                    value={phone}
                    placeholder="-포함"
                    pattern="^\d{2,3}-\d{3,4}-\d{4}$"
                    onChange={inputHandler}
                    required/>
                </Col>
              </Form.Group>
            </Form>
          </div>
        </Card.Body>

        <Card.Footer>
          <Col sm={{ span: 10, offset: 2 }}>
            <Button type="button" onClick={submitHandler} style={{float: "right"}}>정보 변경</Button>
          </Col>
        </Card.Footer>
      </Card>
    </>
  )
}

export default InfoPage;