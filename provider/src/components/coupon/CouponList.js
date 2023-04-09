import {useDispatch, useSelector} from "react-redux";
import React, {useEffect} from "react";
import {Badge, ListGroup} from "react-bootstrap";
import {listCoupon, unLoadCoupons} from "../../modules/coupons";
import client from "../../lib/api/client";
import Loader from "../loader/Loader";


const CouponList = (props) => {

  const dispatch = useDispatch();

  const {coupons, error, loading} = useSelector(({coupons, loading}) => ({
    coupons: coupons.coupons,
    error: coupons.error,
    loading: loading['coupons/LIST_COUPONS'],
  }));

  useEffect(() => {
    client.defaults.headers.common['X-AUTH-TOKEN'] = localStorage.getItem("providerAccessToken");

    dispatch(listCoupon());

    return () => {
      dispatch(unLoadCoupons());
    }
  }, [dispatch, props.isRegModalChanged, props.isUpdateModalChanged]);

  return (
    <>
      {loading && (
        <Loader />
      )}
      {error && (
        <>{error.msg}</>
      )}
      {
        coupons && (
          coupons.map((coupon) => (
              <ListGroup.Item
                key={coupon.id}
                as="li"
                className="d-flex justify-content-between align-items-start coupon-item"
                onClick={() => props.setUpdateModalShow({state: true, coupon: coupon})}
              >

                <div className="ms-2 me-auto content-box">
                  <div className="fw-bold">{coupon.code}</div>
                  {coupon.content}
                </div>

                <div className="badge-box">
                  <Badge bg="info" pill>
                    {coupon.expirationDate}
                  </Badge>
                  <Badge bg="warning" pill>
                    {coupon.discount}
                  </Badge>
                </div>
              </ListGroup.Item>
            )
          )
        )
      }
    </>
  )
}

export default CouponList;