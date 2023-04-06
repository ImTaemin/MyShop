import React, {useCallback, useState} from "react";
import client from "../../lib/api/client";
import {debounce} from "lodash";
import {registerCoupon} from "../../lib/api/coupons";
import {Badge, Button, Form, ListGroup, Modal} from "react-bootstrap";
import {FaCheck, FaTimesCircle} from "react-icons/fa";

const CouponRegisterModal = (props) => {
  const {show, onHide, handleSuccess, handleError} = props;
  const [isCheckCode, setIsCheckCode] = useState(false);

  const today = () => {
    const date = new Date();
    const year = date.getFullYear();
    let month = date.getMonth() + 1;
    const day = date.getDate();

    return `${year}-${month < 10 ? '0' : ''}${month}-${day < 10 ? '0' : ''}${day}`;
  }

  const initForm = {
    code: "",
    content: "",
    expirationDate: today(),
    discount: 0,
  }
  const [formData, setFormData] = useState(initForm);
  const {code, content, expirationDate, discount} = formData;

  const unLoadModal = useCallback(() => {
    setFormData(initForm);
    setIsCheckCode(false);

    onHide(false);
  }, [onHide]);

  // 쿠폰코드 확인
  const debounceCheckCode = useCallback(
    debounce(async (couponCode) => {
      try {
        const response = await client.get(`/coupon/exists/code/${couponCode}`);
        if (response.status === 200) {
          setIsCheckCode(true);
          return;
        }
        setIsCheckCode(false);
      } catch (error) {
        setIsCheckCode(false);
      }
    }, 300), []);

  // 인풋 핸들러
  const inputHandler = useCallback((e) => {
    let {name, value} = e.target;

    if(name === "expirationDate") {
      const inputYear = value.split('-')[0];
      if (inputYear.length > 4) {
        value = today();
      }
    }

    if(name === "code") {
      value = value.replace(/[\\/]/g, '');
      debounceCheckCode(value);
    }

    setFormData((prevFormData) => ({
      ...prevFormData,
      [name]: value,
    }));

  }, [formData, debounceCheckCode]);

  // 쿠폰 등록
  const submitHandler = async (e) => {
    e.preventDefault();

    if(!isCheckCode || code === '') {
      alert("쿠폰 코드 중복을 확인하세요");
      return;
    }

    const reqFormData = new FormData();
    reqFormData.append("code", code.trim());
    reqFormData.append("content", content.trim());
    reqFormData.append("expirationDate", expirationDate.replaceAll("-",".").trim());
    reqFormData.append("discount", discount);

    const response = await registerCoupon(reqFormData);
    try{
      if (response.status === 200) {
        handleSuccess();
        unLoadModal();
        return;
      }
      handleError(response.data.msg);
      unLoadModal();
    } catch (error) {
      handleError(error.data.msg);
      unLoadModal();
    }
  }

  return (
    <Modal
      show={show}
      onHide={unLoadModal}
      size="lg"
      aria-labelledby="contained-modal-title-vcenter"
      centered
    >
      <Modal.Header closeButton>
        <Modal.Title id="contained-modal-title-vcenter">
          쿠폰 등록
        </Modal.Title>
      </Modal.Header>

      <form onSubmit={submitHandler}>
        <Modal.Body>
          <div className="coupon-preview">
            <ListGroup as="ol">
              <ListGroup.Item
                as="li"
                className="d-flex justify-content-between align-items-start coupon-item"
              >
                <div className="ms-2 me-auto content-box">
                  <div className="fw-bold">{code}</div>
                  {content}
                </div>

                <div className="badge-box">
                  <Badge bg="info" pill>
                    {expirationDate}
                  </Badge>
                  <Badge bg="warning" pill>
                    {discount}
                  </Badge>
                </div>
              </ListGroup.Item>
            </ListGroup>
          </div>

          <hr width="80%" style={{marginLeft: "10%"}}/>

          <div className="coupon-form-container">
            <Form.Group className="mb-3" controlId="formBasicCode">
              <Form.Label>코드</Form.Label>
              <div className="debounce-container">
                <Form.Control
                  name="code"
                  type="text"
                  onChange={inputHandler}
                  value={code}
                  pattern='[\w\-]+'
                  placeholder="대소문자, 숫자, 언더바, 하이픈"
                  required
                />
                <div className="debounce-wrap">
                  {isCheckCode && (
                    <FaCheck/>
                  )}
                  {!isCheckCode && (
                    <FaTimesCircle/>
                  )}
                </div>
              </div>
            </Form.Group>

            <Form.Group className="mb-3" controlId="formBasicContent">
              <Form.Label>내용</Form.Label>
              <Form.Control
                name="content"
                type="text"
                onChange={inputHandler}
                value={content}
                placeholder="쿠폰 내용을 입력하세요"
                required />
            </Form.Group>

            <Form.Group className="mb-3" controlId="formBasicExpiration">
              <Form.Label>만료일</Form.Label>
              <Form.Control
                name="expirationDate"
                type="date"
                onChange={inputHandler}
                value={expirationDate}
                required />
            </Form.Group>

            <Form.Group className="mb-3" controlId="formBasicExpiration">
              <Form.Label>할인율</Form.Label>
              <Form.Control
                name="discount"
                type="number"
                onChange={inputHandler}
                value={discount}
                min="0"
                max="100"
                pattern="\d*"
                required />
            </Form.Group>
          </div>
        </Modal.Body>

        <Modal.Footer>
          <Button type="submit">등록</Button>
        </Modal.Footer>
      </form>
    </Modal>
  )
}

export default CouponRegisterModal;