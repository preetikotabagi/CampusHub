import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import FacultyLayout from "../../components/layout/FacultyLayout";
import { getMyCourses } from "../../services/courseService";
import PageHeader from "../../components/common/PageHeader";
import CourseSelectTable from "../../components/common/CourseSelectTable";

function FacultyMarksPage() {

    const [courses, setCourses] = useState([]);
    const [loading, setLoading] = useState(true);
    const navigate = useNavigate();

    useEffect(() => {
        loadCourses();
    }, []);

    const loadCourses = async () => {
        try {
            setLoading(true);
            const data = await getMyCourses();
            setCourses(data);
        } catch (error) {
            console.error(error);
        } finally {
            setLoading(false);
        }
    };

    return (
        <FacultyLayout>

            <PageHeader
                title="Select Course for Marks"
                subtitle="Choose one of your assigned courses to enter marks."
            />

            <CourseSelectTable
                courses={courses}
                loading={loading}
                onSelect={(course) => navigate(`/faculty/courses/${course.id}/marks`)}
                emptyText="You don't have any assigned courses yet."
            />

        </FacultyLayout>
    );
}

export default FacultyMarksPage;
