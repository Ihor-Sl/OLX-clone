import React, {FC, useEffect, useState} from "react";
import {NavLink, useParams} from "react-router-dom";
import {IFullProduct} from "../models/IFullProduct";
import {axiosInstance} from "../axios";
import {Carousel, CarouselItem} from "react-bootstrap";

const ProductPage: FC = () => {

    const {id} = useParams();
    const [product, setProduct] = useState<IFullProduct | null>(null);

    useEffect(() => {
        axiosInstance.get<IFullProduct>(`/products/id/${id}`)
            .then(response => setProduct(response.data));
    }, []);

    if (!product) return <div>Loading...</div>;

    product.photosFileNames.unshift(product.previewPhotoFileName);
    return (
        <div className="container bg-white px-3 pb-3">
            <div className="row">
                <div className="col-md">
                    <Carousel>
                        {product.photosFileNames.map((name, idx) =>
                            <CarouselItem key={idx} style={{backgroundColor: "#888888"}}>
                                <img
                                    className="d-block w-100"
                                    style={{height: "500px", objectFit: "contain"}}
                                    src={`http://localhost:8080/api/v1/images/${name}`}
                                    alt="First slide"
                                />
                            </CarouselItem>
                        )}
                    </Carousel>
                </div>
                <div className="col-md">
                    <h3 className="mt-3 fw-bold">{product.title}</h3>
                    <p className="mb-2">Location: {product.location}</p>
                    <p className="mb-2">Added at: {product.addedAt}</p>
                    <NavLink to={`/users/id/${id}`} className="mb-2">
                        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor"
                             className="bi bi-person-fill" viewBox="0 0 16 16">
                            <path d="M3 14s-1 0-1-1 1-4 6-4 6 3 6 4-1 1-1 1H3Zm5-6a3 3 0 1 0 0-6 3 3 0 0 0 0 6Z"/>
                        </svg>
                        {product.userName}
                    </NavLink>
                    <p className="fs-5 fw-bold">PRICE: {product.price} $</p>
                    <p className="mb-2">{product.description}</p>
                </div>
            </div>
        </div>
    );
};

export default ProductPage;