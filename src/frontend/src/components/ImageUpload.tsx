import React, {FC, useEffect, useState} from "react";

const ImageUpload: FC<{ title?: string, inputName: string }> = ({title, inputName}) => {
    const [selectedFile, setSelectedFile] = useState<File>();
    const [preview, setPreview] = useState<string>();

    useEffect(() => {
        if (!selectedFile) {
            setPreview(undefined);
            return;
        }
        const objectUrl = URL.createObjectURL(selectedFile);
        setPreview(objectUrl);
        return () => URL.revokeObjectURL(objectUrl);
    }, [selectedFile]);

    const onSelectFile = (e: React.ChangeEvent<HTMLInputElement>) => {
        if (!e.target.files || e.target.files.length === 0) {
            setSelectedFile(undefined);
            return;
        }
        setSelectedFile(e.target.files[0]);
    }

    return (
        <div className="mb-3">
            {
                title &&
                <label className="form-label">{title}</label>
            }
            <input name={inputName} onChange={onSelectFile} type="file" className="form-control"/>
            {
                selectedFile &&
                <img className="img-fluid" src={preview} alt="Image"/>
            }
        </div>
    )
}

export default ImageUpload;