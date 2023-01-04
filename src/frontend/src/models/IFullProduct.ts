import {IProduct} from "./IProduct";

export interface IFullProduct extends IProduct {
    photosFileNames: string[],
    userName: string,
    userId: number,
}