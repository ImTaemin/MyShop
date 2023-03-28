import DaumPostcode from "react-daum-postcode";
import {useDispatch} from "react-redux";
import {changeField} from "../../../../modules/orderForm";
import {useCallback} from "react";

const PostcodeModal = (props) => {
  const dispatch = useDispatch();

  const onComplete = useCallback((data) =>{
    const roadAddress = data.roadAddress;
    const postalCode = data.zonecode;

    dispatch(changeField({name:"roadAddress", value:roadAddress}));
    dispatch(changeField({name:"postalCode", value:postalCode}));

    props.closePostcodePopup();
  }, [dispatch]);

  return (
    <div className="postcode-modal">
      <button
        className="btn-close"
        onClick={() => props.closePostcodePopup()}
      />
      <DaumPostcode
        autoClose={false}
        onComplete={onComplete}
      />
    </div>
  );
}

export default PostcodeModal;