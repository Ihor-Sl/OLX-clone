import {IProduct} from "./IProduct";

export interface IUser {
    userId: number | null,
    name: string,
    email: string,
    location: string,
    lastVisit: string,
    products: IProduct[],
}
