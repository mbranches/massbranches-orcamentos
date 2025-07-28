function ErrorMessage({ children }) {
    return (
        <p className='text-red-500 -mt-3 text-[13px]'>
            {children}
        </p>
    );
}

export default ErrorMessage;