import {Alert, Button, Card, ListGroup} from "react-bootstrap";
import React, {useEffect, useState} from "react";
import {TableNav, TableTitle} from "../components/common/Table";
import {BiPurchaseTagAlt} from "react-icons/bi";
import "../scss/Coupon.scss";
import CouponRegisterModal from "../components/coupon/CouponRegisterModal";
import CouponList from "../components/coupon/CouponList";
import CouponUpdateModal from "../components/coupon/CouponUpdateModal";
import {Helmet} from "react-helmet-async";

const CouponPage = () => {

  const [regModalShow, setRegModalShow] = useState(false);
  const [updateModalShow, setUpdateModalShow] = useState({
    state: false,
    coupon: null,
  });
  const [isRegModalChanged, setIsRegModalChanged] = useState(false);
  const [isUpdateModalChanged, setIsUpdateModalChanged] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  useEffect(() =>{
    if(error !== '') {
      setTimeout(() => {
        setError('');
      }, 2000);
    }
  }, [error]);

  useEffect(() =>{
    if(success !== '') {
      setTimeout(() => {
        setSuccess('');
      }, 2000);
    }
  }, [success]);

  return (
    <>
      {success && (
        <div className="alert-box">
          <Alert variant="info" onClose={() => setSuccess('')} dismissible>
            <Alert.Heading>{success}</Alert.Heading>
          </Alert>
        </div>
      )}
      {error && (
        <div className="alert-box">
          <Alert variant="danger" onClose={() => setError('')} dismissible>
            <Alert.Heading>{error}</Alert.Heading>
          </Alert>
        </div>
      )}

      <Helmet>
        <title>쿠폰 관리</title>
      </Helmet>
      <Card>
        <Card.Header>
          <TableNav>
            <TableTitle><BiPurchaseTagAlt style={{marginRight: "0.6em"}} />쿠폰 관리</TableTitle>
            <Button variant="primary" onClick={() => setRegModalShow(true)}>쿠폰 등록</Button>
          </TableNav>
        </Card.Header>

        <Card.Body>
          <ListGroup as="ol" horizontal className="coupon-container">
            <CouponList isRegModalChanged={isRegModalChanged} isUpdateModalChanged={isUpdateModalChanged} setUpdateModalShow={setUpdateModalShow}/>
          </ListGroup>
        </Card.Body>
      </Card>

      {/*모달*/}
      <CouponRegisterModal
        show={regModalShow}
        onHide={() => {
          setIsRegModalChanged(!isRegModalChanged);
          setRegModalShow(!regModalShow);
        }}
        handleSuccess={() => setSuccess("등록 완료")}
        handleError={setError}
      />

      <CouponUpdateModal
        coupon={updateModalShow.coupon}
        show={updateModalShow.state}
        onHide={() => {
          setIsUpdateModalChanged(!isUpdateModalChanged);
          setUpdateModalShow({state: false, coupon: null});
        }}
        handleSuccess={setSuccess}
        handleError={setError}
      />
    </>
  )
}

export default CouponPage;