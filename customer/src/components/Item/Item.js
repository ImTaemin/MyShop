import {Link} from "react-router-dom";

const Item = ({item}) => {

  return (
    <Link to={`/item/${item.id}`}>
      <div className="item-box">
        <div className="item-img">
          <img src={item.mainImage} />
        </div>
        <div className="item-content">
          <p className="brand-name">{item.brandName}</p>
          <p>{item.name}</p>
          <p className="price">{item.price.toLocaleString()}</p>
        </div>
      </div>
    </Link>
  )
}

export default Item;