import React, {FC} from 'react';
import ProductItem from "./ProductItem";
import {IProduct} from "../models/IProduct";

const ProductsHoc: FC<{ products: IProduct[] }> = ({products}) => {
    return (
        <>
            {products.map(p => <ProductItem key={p.id} product={p}/>)}
        </>
    );
};

export default ProductsHoc;