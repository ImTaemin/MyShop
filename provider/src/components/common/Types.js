
export const itemTypes = ["TOP", "OUTER", "PANTS",
  "ONEPIECE", "SKIRT", "SNEAKERS",
  "SHOES", "BAG", "SPORTS",
  "HEADWEAR", "SOCKS_LEGWEAR", "UNDERWEAR",
  "EYEWEAR", "ACCESSORY", "WATCH",
  "JEWELRY", "BEAUTY", "DIGITAL_TECH",
  "LIFE", "CULTURE", "PET"];

export const genderTypes = ["UNISEX", "MEN", "WOMEN"];

export const orderStatusMap = [
  { name: '주문 요청', value: 'REQUESTED', variant: 'outline-secondary' },
  { name: '결제 완료', value: 'PAY_SUCCESS', variant: 'outline-success' },
  { name: '주문 접수', value: 'RECEIVED', variant: 'outline-primary' },
  { name: '주문 취소', value: 'CANCELED', variant: 'outline-danger' },
  { name: '배송 진행', value: 'DELIVERING', variant: 'outline-warning' },
  { name: '배송 완료', value: 'DELIVERED', variant: 'outline-dark' },
];

export const getNameByValue = (value) => {
  const result = orderStatusMap.find((element) => element.value === value);
  return result ? result.name : null;
}
