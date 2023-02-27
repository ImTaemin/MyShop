import {Button, ButtonGroup, Card, FormCheck, Table, ToggleButton} from "react-bootstrap";
import {BiClipboard} from "react-icons/bi";
import {TableNav, TableTitle} from "../components/common/Table";


const ItemPage = () => {
  return (
    <Card>
      <Card.Header>
        <TableNav>
          <TableTitle><BiClipboard style={{marginRight: "0.6em"}} />상품 관리</TableTitle>
          <Button variant="primary">상품 등록</Button>
        </TableNav>
      </Card.Header>
      <Card.Body>
        <Table hover="true">
          <thead>
          <tr align="center">
            <th width="2%">　</th>
            <th width="5%">상품</th>
            <th width="20%">상품명 / 상품코드</th>
            <th width="6%">가격</th>
            <th width="5%">재고</th>
          </tr>
          </thead>

          <tbody>
          <tr align="center">
            <td>
              <FormCheck
                type="checkbox"
                id={`default-checkbox`}
              />
            </td>
            <td>
              <img
                src="https://image.msscdn.net/images/goods_img/20220816/2717595/2717595_1_320.jpg"
                className="rounded-3"
                width="50"
                height="50"
              />
            </td>
            <td> 벨리곰X위글위글 쿠션담요<br/> PWIKR410062</td>
            <td>3500</td>
            <td>30</td>
          </tr>
          </tbody>
        </Table>
      </Card.Body>
    </Card>
  )
}

export default ItemPage;