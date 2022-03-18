declare interface ICartItem {
  id: string;
  price: number;
  name: string;
  quantity: number;
  savings: number;
  discount: number;
  perUnit: string;
}

declare interface ICart {
  items: ICartItem[];
}

declare interface IPriceDetails {
  price: number;
  savings: number;
}

declare type Renderer = (cart: ICart) => void;

declare interface IOrderStatus {
  status: string;
}
