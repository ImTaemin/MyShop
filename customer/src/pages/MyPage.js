import {Button, Card, Col, Form, Row} from "react-bootstrap";
import {useCallback, useEffect} from "react";
import {useDispatch, useSelector} from "react-redux";
import {changeField, loadAuthInfo, unLoadAuthInfo} from "../modules/mypage";
import client from "../lib/api/client";
import Loader from "../components/loader/Loader";
import {submitAuthInfo} from "../lib/api/auth";

const MyPage = () => {
  const dispatch = useDispatch();

  const {mypage, loading} = useSelector(state => ({
    ...state,
    loading: state.loading["mypage/LOAD_AUTH_INFO"],
  }));
  const {userId, password, modifyPassword, phone, name, createDate} = mypage || mypage;

  const inputHandler = useCallback((e) => {
    const {name, value} = e.target;

    dispatch(changeField({name,value}));

  }, [dispatch]);

  const submitHandler = useCallback((e) => {
    submitAuthInfo(mypage || mypage)
      .then(response => {
        if(response.status === 200) {
          alert("정보를 수정했습니다.");
        } else {
          alert("정보를 수정하지 못했습니다.");
        }
        window.location.reload();
      })
      .catch(error => {
        alert("정보를 수정하지 못했습니다.");
        window.location.reload();
      });
  }, [dispatch, mypage]);

  useEffect(() => {
    client.defaults.headers.common['X-AUTH-TOKEN'] = localStorage.getItem("customerAccessToken");

    dispatch(loadAuthInfo());

    return () => {
      dispatch(unLoadAuthInfo());
    }
  }, []);

  return (
    <div className="p-5">
      {loading && (<Loader />)}
      <Card>
        <Card.Header>
          {name || name} / 가입일 : {createDate || createDate}
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
                    value={userId || userId}
                    style={{background: "#eeeeee"}}
                    readOnly/>
                </Col>
              </Form.Group>

              <Form.Group as={Row} className="mb-3" controlId="formPassword">
                <Form.Label column sm={2}>
                  현재 비밀번호
                </Form.Label>
                <Col sm={10}>
                  <Form.Control
                    name="password"
                    type="password"
                    value={password || password}
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
                    value={modifyPassword || modifyPassword}
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
                    value={phone || phone}
                    maxLength={13}
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
    </div>
  );
}

export default MyPage;