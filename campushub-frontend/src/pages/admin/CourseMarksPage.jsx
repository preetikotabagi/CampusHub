import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import AdminLayout from "../../components/layout/AdminLayout";
import { getAllCourses } from "../../services/courseService";
import PageHeader from "../../components/common/PageHeader";
import CourseSelectTable from "../../components/common/CourseSelectTable";

function CourseMarksPage() {

    const [courses, setCourses] = useState([]);
    const [loading, setLoading] = useState(true);
    const navigate = useNavigate();

    useEffect(() => {
        loadCourses();
    }, []);

    const loadCourses = async () => {
        try {
            setLoading(true);
            const data = await getAllCourses();
            setCourses(data);
        } catch (error) {
            console.error(error);
        } finally {
            setLoading(false);
        }
    };

    return (

        <AdminLayout>

            <PageHeader
                title="Select Course for Marks"
                subtitle="Choose a course to view its marks records."
            />

            <CourseSelectTable
                courses={courses}
                loading={loading}
                onSelect={(course) => navigate(`/admin/courses/${course.id}/marks`)}
                emptyText="No courses have been created yet."
            />

        </AdminLayout>

    );
}

export default CourseMarksPage;
