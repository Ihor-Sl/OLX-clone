import React, {FC} from "react";
import {IProduct} from "../models/IProduct";
import {NavLink} from "react-router-dom";

const ProductItem: FC<{product: IProduct}> = ({product}) => {
    const {id, title, description, price, location, previewPhotoFileName, addedAt} = product;
    return (
        <div className="row bg-white rounded p-2 mb-3">
            <div className="col-md-12 col-lg-3 mb-3 mb-lg-0">
                <NavLink to={`/products/id/${id}`}>
                    <img
                        style={{height: "200px", objectFit: "contain"}}
                        src={`http://localhost:8080/api/v1/images/${previewPhotoFileName}`}
                        className="w-100"
                        alt="Preview photo"
                    />
                </NavLink>
            </div>
            <div className="col-md-6 col-lg-6">
                <h5>{title}</h5>
                <div className="mt-1 mb-2 text-muted small">
                    <p className="mb-1">{location}</p>
                    <p>{addedAt}</p>
                </div>
                <p className="text-truncate mb-4 mb-md-0">{description}</p>
            </div>
            <div className="col-md-6 col-lg-3 border-start">
                <h4 className="mt-2">{price} $</h4>
                <div className="d-flex flex-column mt-4">
                    <NavLink to={`/products/id/${id}`} className="btn btn-primary btn-sm" type="button">
                        Details
                    </NavLink>
                    <button className="btn btn-outline-primary btn-sm mt-2" type="button">
                        Add to wishlist
                    </button>
                </div>
            </div>
        </div>
    )
};

export default ProductItem;