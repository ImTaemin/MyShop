
export const itemType = [
  { type: "TOP", title: "상의"},
  { type: "OUTER", title: "아우터"},
  { type: "PANTS", title: "바지"},
  { type: "ONEPIECE", title: "원피스"},
  { type: "SKIRT", title: "스커트"},
  { type: "SNEAKERS", title: "스니커즈"},
  { type: "SHOES", title: "신발"},
  { type: "BAG", title: "가방"},
  { type: "SPORTS", title: "스포츠/용품"},
  { type: "HEADWEAR", title: "모자"},
  { type: "SOCKS_LEGWEAR", title: "양말/레그웨어"},
  { type: "UNDERWEAR", title: "속옷"},
  { type: "EYEWEAR", title: "선글라스/안경테"},
  { type: "ACCESSORY", title: "액세서리"},
  { type: "WATCH", title: "시계"},
  { type: "JEWELRY", title: "주얼리"},
  { type: "BEAUTY", title: "뷰티"},
  { type: "DIGITAL_TECH", title: "디지털/테크"},
  { type: "LIFE", title: "리빙"},
  { type: "CULTURE", title: "컬처"},
  { type: "PET", title: "반려동물"},
];

export const genderType = ["UNISEX", "MEN", "WOMEN"];

export const orderStatusMap = [
  { name: '주문 요청', value: 'REQUESTED'},
  { name: '결제 완료', value: 'PAY_SUCCESS'},
  { name: '주문 접수', value: 'RECEIVED'},
  { name: '주문 취소', value: 'CANCELED'},
  { name: '배송 진행', value: 'DELIVERING'},
  { name: '배송 완료', value: 'DELIVERED'},
];

export const getNameByValue = (value) => {
  const result = orderStatusMap.find((element) => element.value === value);
  return result ? result.name : null;
}