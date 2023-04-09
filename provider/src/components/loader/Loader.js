import Spinner from 'react-bootstrap/Spinner';
import styled from "styled-components";

const LoaderWrapper = styled.div`
  width: 100%;
  display: flex;
  justify-content: center;
`;

function Loader() {
  return (
    <LoaderWrapper>
      <Spinner animation="border" role="status">
        <span className="visually-hidden">Loading...</span>
      </Spinner>
    </LoaderWrapper>
  );
}

export default Loader;