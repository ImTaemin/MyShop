import styled from "styled-components";

export const ImgNameWrap = styled.div`
  padding: 0 0% 0 20%;
  display: flex;
  align-items: center;
`;

export const CustomCheckBox = styled.input`
  &[type='checkbox'] {
    transform : scale(2);
    accent-color: forestgreen;
  }
`;

export const ItemNameDiv = styled.div`
  margin-left: 5%;
  width: 100%;
  word-break:break-all;
`;