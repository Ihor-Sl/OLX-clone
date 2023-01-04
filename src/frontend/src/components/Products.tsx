import React, {FC, useEffect, useState} from "react";
import {IProduct} from "../models/IProduct";
import {axiosInstance} from "../axios";
import {IPageableResponse} from "../models/IPageableResponse";
import ReactPaginate from "react-paginate";
import ProductsHOC from "./ProductsHOC";

const Products: FC = () => {

    const [pageableResponse, setProducts] = useState<IPageableResponse<IProduct> | null>(null);
    const [isLoading, setIsLoading] = useState<boolean>(false);
    const [currentPage, setCurrentPage] = useState<number>(1);

    useEffect(() => {
        setIsLoading(true);
        axiosInstance.get<IPageableResponse<IProduct>>(`/products?page=${currentPage}`)
            .then(response => {
                setProducts(response.data);
            })
            .finally(() => {
                setIsLoading(false)
            })
    }, [currentPage]);

    if (!pageableResponse) return <div>Loading...</div>

    return (
        <div className="my-4 px-5 container">
            {isLoading ? <div>Loading...</div> : <ProductsHOC products={pageableResponse.content}/>}
            <ReactPaginate
                nextLabel={"Next"}
                previousLabel={"Previous"}
                breakLabel={"..."}
                pageCount={pageableResponse.totalPages}
                marginPagesDisplayed={2}
                pageRangeDisplayed={3}
                onPageChange={({selected}) => setCurrentPage(selected)}
                containerClassName={"pagination justify-content-center"}
                pageClassName={"page-item"}
                pageLinkClassName={"page-link"}
                previousClassName={"page-item"}
                previousLinkClassName={"page-link"}
                nextClassName={"page-item"}
                nextLinkClassName={"page-link"}
                breakClassName={"page-item"}
                breakLinkClassName={"page-link"}
                activeClassName={"active"}
            />
        </div>
    );
};

export default Products;