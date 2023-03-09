import {useSelector} from "react-redux";
import {Button, Modal} from "react-bootstrap";
import {useCallback} from "react";
import {deleteItems} from "../../lib/api/items";

const ItemDeleteModal = (props) => {
  const {checkItemIds} = useSelector(({items}) => ({
    checkItemIds: items.checkItemIds
  }));

  const deleteCheckItems = useCallback(async () => {
    const response = await deleteItems(checkItemIds);
    if(response.status === 200) {
      props.handleSuccess()
    } else {
      props.handleError(response.data.msg);
    }
    props.onHide();
  }, [checkItemIds]);

  return (
    <Modal show={props.show} onHide={props.onHide}>
      <Modal.Header closeButton>
        <Modal.Title>상품 삭제</Modal.Title>
      </Modal.Header>
      <Modal.Body>삭제하시겠습니까?</Modal.Body>
      <Modal.Footer>
        <Button variant="secondary" onClick={props.onHide}>
          닫기
        </Button>
        <Button variant="primary" onClick={deleteCheckItems}>
          삭제
        </Button>
      </Modal.Footer>
    </Modal>
  )
}

export default ItemDeleteModal;