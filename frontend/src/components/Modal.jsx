import { useEffect } from "react";

function Modal({ modalIsOpen, setModalIsOpen, children }) {
    useEffect(() => {
        if(modalIsOpen) {
            document.body.style.overflow = "hidden";
        } else {
            document.body.style.overflow = "unset";
        }
        
        return () => document.body.style.overflow = "unset";
    }, [modalIsOpen]);

    return (
        <div 
            className='fixed inset-0 z-40 bg-black/80 flex justify-center items-center' 
            onClick={() => setModalIsOpen(false)}
        >
            <div 
                className='bg-white p-6 rounded-lg shadow'
                onClick={e => e.stopPropagation()}
            >
                { children }
            </div>
        </div>
    );
}

export default Modal;