import {itemType} from "../../common/Types";
import "../../assets/scss/category.scss";
import {Link} from "react-router-dom";

const Category = () => {

  return (
    <div className="category-container">
      <div className="category-list">
        {itemType.map(({type, title}, index) => (
          <Link to={`/category/${type}`} key={index}>
            <div className="category-wrap">
              <span className="title">{title}</span>
              <span className="type">
                {/*첫 글자만 대문자로 변경*/}
                {type.toLowerCase()
                  .replace("_", "/")
                  .replace(/\b[a-z]/g, char => char.toUpperCase())}
              </span>
            </div>
          </Link>
        ))}
      </div>
    </div>
  )
}

export default Category;