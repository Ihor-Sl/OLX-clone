export interface IPageableResponse<T> {
    totalPages: number,
    totalElements: number,
    content: T[]
}