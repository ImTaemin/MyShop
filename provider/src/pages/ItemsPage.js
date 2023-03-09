import {Alert, Button, Card} from "react-bootstrap";
import "../scss/Alert.scss";
import {BiGridAlt} from "react-icons/bi";
import {TableNav, TableTitle} from "../components/common/Table";
import ItemList from "../components/items/ItemList";
import React, {useState} from "react";
import ItemRegisterModal from "../components/items/ItemRegisterModal";
import styled from "styled-components";
import {useSelector} from "react-redux";
import ItemDeleteModal from "../components/items/ItemDeleteModal";

const TableControlDiv = styled.div`
  
  & > button:nth-child(1) {
    margin-right: 10px;  
  }
`;

const ItemsPage = () => {
  const [regModalShow, setRegModalShow] = useState(false);
  const [delModalShow, setDelModalShow] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [isRegModalChanged, setIsRegModalChanged] = useState(false);
  const [isDelModalChanged, setIsDelModalChanged] = useState(false);

  const {checkItemIds} = useSelector(({items}) => ({
    checkItemIds: items.checkItemIds
  }));

  const handleDelModalShow = () => {
    if(checkItemIds > 0 ) {
      setDelModalShow(true);
    } else {
      setError("상품을 먼저 선택하세요")
    }
  }

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

      <Card style={{height: "100%"}}>
        <Card.Header>
          <TableNav>
            <TableTitle><BiGridAlt style={{marginRight: "0.6em"}} />상품 관리</TableTitle>
            <TableControlDiv>
              <Button variant="primary" onClick={() => setRegModalShow(true)}>상품 등록</Button>
              <Button variant="danger" onClick={handleDelModalShow}>선택 삭제</Button>
            </TableControlDiv>
          </TableNav>
        </Card.Header>

        <Card.Body>
          <ItemList
            isRegModalChanged={isRegModalChanged}
            isDelModalChanged={isDelModalChanged}
            handleSuccess={setSuccess}
            handleError={setError}
          />
        </Card.Body>
      </Card>

      {/* 모달 */}
      <ItemRegisterModal
        show={regModalShow}
        onHide={() => {
          setIsRegModalChanged(!isRegModalChanged);
          setRegModalShow(false);
        }}
        handleSuccess={() => setSuccess("등록 완료")}
        handleError={setError}
      />

      <ItemDeleteModal
        show={delModalShow}
        onHide={() => {
          setIsDelModalChanged(!isDelModalChanged);
          setDelModalShow(false);
        }}
        handleSuccess={() => setSuccess("삭제 완료")}
        handleError={setError}
      />
    </>
  )
}

export default ItemsPage;