import React, {FC, useRef, useState} from 'react';
import ImageUpload from "./ImageUpload";
import {useAppDispatch} from "../redux/hooks";
import {addProduct} from "../redux/reducers/userReducer";

const NewProduct: FC = () => {

    const dispatch = useAppDispatch();

    const [title, setTitle] = useState<string>("");
    const [description, setDescription] = useState<string>("");
    const [location, setLocation] = useState<string>("");
    const price = useRef<HTMLInputElement>(null);

    const handleSubmit = (e: any) => {
        e.preventDefault()
        const formData = new FormData(e.target)
        const product = JSON.stringify({
            title,
            description: description === "" ? null : description,
            location,
            // @ts-ignore
            price: Number.parseInt(price.current.value)
        });
        const blob = new Blob([product], {type: "application/json"})
        formData.append("product", blob);
        // @ts-ignore
        for (const entry of formData.entries()) {
            console.log(entry[0] + ", " + entry[1])
        }
        dispatch(addProduct(formData))
    }
    return (
        <form onSubmit={(e) => handleSubmit(e)} className="mt-5 m-auto" style={{width: "600px"}}>
            <h3 className="text-center">New product</h3>
            <div className="mb-3">
                <label htmlFor="title" className="form-label">Title</label>
                <input value={title}
                       onChange={(e: React.ChangeEvent<HTMLInputElement>) => setTitle(e.target.value)}
                       type="text" className="form-control" id="title"
                />
            </div>
            <div className="mb-3">
                <label htmlFor="description" className="form-label">
                    Description <span className="text-muted">(max 2000 characters)</span>
                </label>
                <textarea value={description}
                          onChange={(e: React.ChangeEvent<HTMLTextAreaElement>) => setDescription(e.target.value)}
                          className="form-control" id="description" rows={3}
                />
            </div>
            <div className="mb-3">
                <label htmlFor="location" className="form-label">Location</label>
                <input value={location}
                       onChange={(e: React.ChangeEvent<HTMLInputElement>) => setLocation(e.target.value)}
                       type="text" className="form-control" id="location"
                />
            </div>
            <div className="mb-3">
                <label htmlFor="price" className="form-label">Price</label>
                <input
                    ref={price}
                    type="number" className="form-control" id="price"
                />
            </div>
            <div className="mb-3">
                <ImageUpload inputName="previewPhoto" title="Preview photo"/>
                <ImageUpload inputName="photo2" title="Photos"/>
                <ImageUpload inputName="photo3"/>
                <ImageUpload inputName="photo4"/>
                <ImageUpload inputName="photo5"/>
                <ImageUpload inputName="photo6"/>
                <ImageUpload inputName="photo7"/>
                <ImageUpload inputName="photo8"/>
                <ImageUpload inputName="photo9"/>
                <ImageUpload inputName="photo10"/>
            </div>
            <button type="submit">Submit</button>
        </form>
    );
};

export default NewProduct;